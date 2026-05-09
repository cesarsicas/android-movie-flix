package br.com.cesarsicas.androidmovieflix.presentation.navigation

object Routes {
    const val HOME = "home"

    const val AUTH_MODE_ARG = "mode"
    const val AUTH_TEMPLATE = "auth?$AUTH_MODE_ARG={$AUTH_MODE_ARG}"
    fun auth(mode: String = "login") = "auth?$AUTH_MODE_ARG=$mode"

    const val TITLE_DETAILS_ARG = "externalId"
    const val TITLE_DETAILS_TEMPLATE = "title/details/{$TITLE_DETAILS_ARG}"
    fun titleDetails(externalId: Int) = "title/details/$externalId"

    const val TITLE_SEARCH_ARG = "query"
    const val TITLE_SEARCH_TEMPLATE = "title/search?$TITLE_SEARCH_ARG={$TITLE_SEARCH_ARG}"
    fun titleSearch(query: String) = "title/search?$TITLE_SEARCH_ARG=$query"

    const val PERSON_DETAILS_ARG = "personId"
    const val PERSON_DETAILS_TEMPLATE = "person/{$PERSON_DETAILS_ARG}"
    fun personDetails(personId: Int) = "person/$personId"

    const val BROWSE = "browse"
    const val CHAT = "chat"

    const val PROFILE = "profile"
    const val PROFILE_EDIT = "profile/edit"
    const val WATCH_PARTY = "watch-party"

    const val ADMIN_ENTRY = "admin"
    const val ADMIN_LOGIN = "admin/login"
    const val ADMIN_HOME = "admin/home"
    const val ADMIN_WATCH_PARTY = "admin/watch-party"
    const val ADMIN_NEW_TRANSMISSION = "admin/watch-party/new"
    const val ADMIN_UPLOAD_MOVIE = "admin/watch-party/upload"
}
