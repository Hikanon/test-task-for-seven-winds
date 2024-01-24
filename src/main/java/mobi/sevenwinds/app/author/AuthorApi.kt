package mobi.sevenwinds.app.author

import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.post
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route

fun NormalOpenAPIRoute.author() {
    route("/author/add").post<Unit, AuthorRecordResponse, AuthorRecordRequest>(info("Добавить нового автора")) { _, body ->
        respond(AuthorService.addRecord(body))
    }

}

data class AuthorRecordResponse(
    val fullName: String,
    val createDateTime: String
)

data class AuthorRecordRequest(
    val fullName: String,
)