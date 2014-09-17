package hu.gansperger.neptunapi

import com.ning.http.client.cookie.Cookie

case class Session(URL: String, cookies: List[Cookie], userAgent : String)
