package org.jitsi.jibri.selenium

import org.jitsi.jibri.CallUrlInfo
import org.jitsi.jibri.selenium.pageobjects.CallPage
import org.jitsi.jibri.selenium.pageobjects.HomePage
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

/**
 * The [JibriSelenium] class is responsible for all of the interactions with
 * Selenium.  It:
 * 1) Owns the webdriver
 * 2) Handles passing the proper options to Chrome
 * 3) Sets the necessary localstorage variables before joining a call
 */
class JibriSelenium(jibriSeleniumOptions: JibriSeleniumOptions)
{
    var chromeDriver: ChromeDriver
    var baseUrl: String

    /**
     * Set up default chrome driver options (using fake device, etc.)
      */
    init {
        baseUrl = jibriSeleniumOptions.baseUrl
        System.setProperty("webdriver.chrome.driver", "/usr/local/Cellar/chromedriver/2.34/bin/chromedriver")
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments(
                "--use-fake-ui-for-media-stream",
                "--start-maximized",
                "--kiosk",
                "--enabled",
                "--enable-logging",
                "--vmodule=*=3",
                "--disable-infobars",
                "--alsa-output-device=plug:amix"
        )
        if (jibriSeleniumOptions.customBinaryLocation != null)
        {
            chromeOptions.addArguments(jibriSeleniumOptions.customBinaryLocation)
        }
        chromeDriver = ChromeDriver(chromeOptions)
    }

    /**
     * Set various values to be put in local storage.  NOTE: the driver
     * should have already navigated to the desired page
     */
    private fun setJibriIdentifiers(vararg keyValues: Pair<String, String>)
    {
        for ((key, value) in keyValues)
        {
            chromeDriver.executeScript("window.localStorage.setItem('" + key + "', '" + value + "')");
        }
    }

    /**
     * Join a
     */
    fun joinCall(callName: String)
    {
        HomePage(chromeDriver).visit(CallUrlInfo(baseUrl, ""))
        setJibriIdentifiers(
                Pair("displayname", "TODO"),
                Pair("email", "TODO"),
                Pair("xmpp_username_override", "TODO"),
                Pair("xmpp_password_override", "TODO"),
                Pair("callStatsUserName", "jibri")
        )
        CallPage(chromeDriver).visit(CallUrlInfo(baseUrl, callName))
    }

    fun leaveCallAndQuitBrowser()
    {
        CallPage(chromeDriver).leave()
        chromeDriver.quit()
    }

    //TODO: helper func to verify connectivity
}