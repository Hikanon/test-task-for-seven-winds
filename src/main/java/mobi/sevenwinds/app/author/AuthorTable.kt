package mobi.sevenwinds.app.author

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.joda.time.DateTime

object AuthorTable : IntIdTable("author") {
    val fullName = varchar("full_name", 100)
    val createDate = datetime("create_date").default(DateTime.now())
}

class AuthorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AuthorEntity>(AuthorTable)

    var fullName by AuthorTable.fullName
    var createDate by AuthorTable.createDate

    fun toResponse(): AuthorRecordResponse {
        return AuthorRecordResponse(fullName, createDate.toString())
    }
}