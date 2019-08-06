require 'json'

version = JSON.parse(File.read('package.json'))["version"]

Pod::Spec.new do |s|
  s.name         = "react-native-pjsip"
  s.version      = version
  s.summary      = "PJSIP module for React Native"
  s.homepage     = "https://github.com/nvojnovic/react-native-pjsip"
  s.license      = "MIT"
  s.author       = { "author" => "vadim.ruban@carusto.com" }
  s.platform     = :ios, "8.0"
  s.source       = { :git => "https://github.com/nvojnovic/react-native-pjsip.git", :tag => "master" }
  s.pod_target_xcconfig = { 'GCC_PREPROCESSOR_DEFINITIONS' => 'PJ_AUTOCONF=1' }
  s.vendored_frameworks = 'ios/VialerPJSIP.framework'
  s.source_files  = "ios/RTCPjSip/*.{h,m}"
  s.preserve_paths = "**/*.js"
  s.requires_arc = true

  s.dependency "React"
end

