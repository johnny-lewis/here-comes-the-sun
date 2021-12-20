package au.com.lexicon.herecomesthesun.domain.usecase

import au.com.lexicon.herecomesthesun.domain.service.Permission
import javax.inject.Inject

class ResolveLocationPermissionUseCase @Inject constructor(
    private val permission: Permission
) {
    operator fun invoke(callback: (Boolean) -> Unit) {
        if (permission.hasLocationPermission()) {
            callback(true)
        } else {
            permission.requestLocationPermission(callback)
        }
    }
}
