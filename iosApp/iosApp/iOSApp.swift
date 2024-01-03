import SwiftUI
import shared

@main
struct iOSApp: App {

    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}

}

class AppDelegate: NSObject, UIApplicationDelegate, ObservableObject {

    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        let tokenParts = deviceToken.map { data in String(format: "%02.2hhx", data) }
        let token = tokenParts.joined()
//        Main_iosKt.setupNotificationToken(token : token)
        Main_iosKt.setupAddDeviceInput(addDeviceInput :AddDeviceInput(
            brand: GeneralInfo.Device.brand.value,
            appVersion: GeneralInfo.App.version.value,
            isActive: true,
            deviceId: GeneralInfo.Device.identifier.value,
            deviceName: GeneralInfo.Device.name.value,
            operatingSystem: 2,
            deviceToken: token,
            operatingSystemVersion: GeneralInfo.Device.osVersion.value)
        )
    }
}
