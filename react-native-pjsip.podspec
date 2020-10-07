require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = package['name']
  s.version      = package['version']
  s.summary      = package['description']
  s.license      = package['license']

  s.authors      = package['author']
  s.homepage     = package['homepage']
  s.platform     = :ios, "9.0"

  s.source       = { :git => package["repository"]["url"], :tag => "without-postinstall" }
  s.pod_target_xcconfig = { 'GCC_PREPROCESSOR_DEFINITIONS' => 'PJ_AUTOCONF=1' }
  s.vendored_frameworks = 'ios/VialerPJSIP.framework'
  s.source_files  = "ios/RTCPjSip/*.{h,m}"

  s.dependency 'React-Core'
end
