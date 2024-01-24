package mobi.sevenwinds.app.author

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction

object AuthorService {
    suspend fun addRecord(body: AuthorRecordRequest): AuthorRecordResponse = withContext(Dispatchers.IO) {
        transaction {
            val entity = AuthorEntity.new {
                this.fullName = body.fullName
            }

            return@transaction entity.toResponse()
        }
    }

    fun getRecord(id: Int): AuthorRecordResponse?{
        return transaction {
            val entity = AuthorEntity.findById(id)

            return@transaction entity?.toResponse()
        }
    }
}