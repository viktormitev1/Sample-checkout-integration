package com.example.wcointegration

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Checkout launch point.
 * Note the launchMode="singleTop" in order to preserve the app state on redirecting back to the application.
 */
class WcoLauncherActivity : AppCompatActivity() {

    private val goToCheckoutButton: Button by lazy {
        findViewById(R.id.btn_go_to_checkout)
    }

    private val statusTextView: TextView by lazy {
        findViewById(R.id.tv_status)
    }

    /**
     * Called on redirecting back to the application.
     * onNewIntent() or onCreate() is called depending on the Activity launchMode.
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        maybeHandleCheckoutRedirect(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wco_launcher)

        maybeHandleCheckoutRedirect(intent)

        goToCheckoutButton.setOnClickListener {
            it.visibility = View.GONE
            // TODO openInBrowser() with generated Checkout url

            // The url have to contain return_url parameter in order for the app to be called back when the Checkout is done.
            // Alternatively url with Session ID as parameter could be loaded. It's still a valid approach as long as
            // return_url parameter is passed to the request that returns Skrill Wallet Checkout session ID

            // For more options refer to Skrill Checkout integration guide at https://www.skrill.com/en/business/integration/

            // This sample uses return_url=https://viktormitev1.github.io which is registered as Android App Links.

//            openInBrowser(
//                Uri.parse(
//                    "https://pay.eu-qa.sandbox.dw-cloud.net/" +
//                            "?merchant_id=326390328&amount=6" +
//                            "&currency=USD" +
//                            "&pay_from_email=us.customer.1243@sun-fish.com" +
//                            "&payment_methods=WLT" +
//                            "&return_url=https://viktormitev1.github.io"
//                )
//            )

            Toast.makeText(this, R.string.prompt_load_checkout, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * When the Wallet Checkout is done the user will get redirected to your application.
     *
     * For this to work, a number of things need to be set up:
     * 1. Provide your domain as a redirect_url parameter for the Checkout url
     * 2. Add an intent filter to receive redirects to the Android manifest.
     *    In this example, replace the value of the resource `R.string.your_domain` with your domain
     * 3. Host a `.well-known/assetlinks.json` file on your domain as explained here:
     *    https://developer.android.com/training/app-links/verify-android-applinks
     *    Step-by-step guide how to add Android App Links:
     *    https://developer.android.com/studio/write/app-link-indexing
     */
    private fun maybeHandleCheckoutRedirect(intent: Intent?) {
        if (intent?.data?.host == getString(R.string.your_domain)) {
            statusTextView.visibility = View.VISIBLE
            // redirect from Skrill Checkout, maybe poll for transaction status
        }
    }

    private fun openInBrowser(uri: Uri) {
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}
