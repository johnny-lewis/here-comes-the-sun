package au.com.lexicon.herecomesthesun.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import au.com.lexicon.herecomesthesun.R
import au.com.lexicon.herecomesthesun.domain.service.Permission
import au.com.lexicon.herecomesthesun.presentation.NavigationGraph.Routes
import au.com.lexicon.herecomesthesun.service.PermissionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var permission: Permission

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permission.provideActivityContext(this)

        setContent {
            NavigationGraph(
                activity = this
            ).Start(
                destination = Routes.HomeScreen
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionService.LOCATION_REQUEST_CODE -> permission.onRequestLocationFinished(
                allowed = grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
            )
        }
    }
}
