package mobi.sevenwinds.app.budget

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobi.sevenwinds.app.author.AuthorTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object BudgetService {
    suspend fun addRecord(body: BudgetRecordRequest): BudgetRecordResponse = withContext(Dispatchers.IO) {
        transaction {
            val entity = BudgetEntity.new {
                this.year = body.year
                this.month = body.month
                this.amount = body.amount
                this.type = body.type
                this.authorId = EntityID(body.authorId, AuthorTable).takeIf { it._value != null }
            }

            return@transaction entity.toResponse()
        }
    }

    suspend fun getYearStats(param: BudgetYearParam): BudgetYearStatsResponse = withContext(Dispatchers.IO) {
        transaction {
            val query: Query

            if (param.fullName != null && param.fullName != "") {
                query = (BudgetTable leftJoin AuthorTable)
                    .select {
                        BudgetTable.year eq param.year
                        AuthorTable.fullName.like("%" + param.fullName.toLowerCase() + "%")
                    }
                    .limit(param.limit, param.offset)
                    .orderBy(BudgetTable.month, SortOrder.ASC)
                    .orderBy(BudgetTable.amount, SortOrder.DESC)
            } else {
                query = (BudgetTable leftJoin AuthorTable)
                    .select {
                        BudgetTable.year eq param.year
                    }
                    .limit(param.limit, param.offset)
                    .orderBy(BudgetTable.month, SortOrder.ASC)
                    .orderBy(BudgetTable.amount, SortOrder.DESC)
            }

            val total = query.count()
            val data = BudgetEntity.wrapRows(query).map { it.toResponse() }

            val sumByType = data.groupBy { it.type.name }.mapValues { it.value.sumOf { v -> v.amount } }

            return@transaction BudgetYearStatsResponse(
                total = total,
                totalByType = sumByType,
                items = data
            )
        }
    }
}