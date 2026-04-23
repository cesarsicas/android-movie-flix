package br.com.cesarsicas.androidmovieflix.presentation.navigation

/**
 * Route strings for all destinations in the app.
 * Argument-taking routes provide a builder helper alongside the template.
 */
object Routes {
    const val HOME = "home"
    const val AUTH = "auth"

    const val TITLE_DETAILS_ARG = "externalId"
    const val TITLE_DETAILS_TEMPLATE = "title/{$TITLE_DETAILS_ARG}"
    fun titleDetails(externalId: String) = "title/$externalId"

    const val TITLE_SEARCH_ARG = "query"
    const val TITLE_SEARCH_TEMPLATE = "title/search?$TITLE_SEARCH_ARG={$TITLE_SEARCH_ARG}"
    fun titleSearch(query: String) = "title/search?$TITLE_SEARCH_ARG=$query"

    const val PROFILE = "profile"
    const val PROFILE_EDIT = "profile/edit"
    const val WATCH_PARTY = "watch-party"

    const val ADMIN_ENTRY = "admin"
    const val ADMIN_LOGIN = "admin/login"
    const val ADMIN_HOME = "admin/home"
    const val ADMIN_WATCH_PARTY = "admin/watch-party"
    const val ADMIN_NEW_TRANSMISSION = "admin/watch-party/new-transmission"
    const val ADMIN_UPLOAD_MOVIE = "admin/watch-party/upload-movie"
}
