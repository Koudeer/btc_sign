#import "BtcSignPlugin.h"
#if __has_include(<btc_sign/btc_sign-Swift.h>)
#import <btc_sign/btc_sign-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "btc_sign-Swift.h"
#endif

@implementation BtcSignPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftBtcSignPlugin registerWithRegistrar:registrar];
}
@end
