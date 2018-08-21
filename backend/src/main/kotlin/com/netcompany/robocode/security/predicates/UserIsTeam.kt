package com.netcompany.robocode.security.predicates

import com.netcompany.robocode.auth.UserBean
import com.netcompany.robocode.security.SecurityPredicate
import org.springframework.stereotype.Component
import java.util.*


@Component
class UserIsTeam(private val userBean: UserBean) : SecurityPredicate {
    init {
        Objects.requireNonNull(userBean)
    }

    /**
     * Checks if the user is an admin user.
     *
     * @param params The parameters the method is receiving.
     * @return True if the user is an admin, false otherwise.
     */
    override fun checkAccess(params: Map<String, Any>): Boolean {
        return if (!userBean.isLoggedIn) {
            false
        } else {
            return userBean.claims?.get("type", String::class.java)?.equals("team") ?: false
        }

    }
}