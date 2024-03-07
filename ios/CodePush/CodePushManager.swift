import Foundation

@objc(CodePushManager)
class CodePushManager: NSObject {
  @objc static let shared = CodePushManager()
  
  private let diskManager = DiskManager.shared
  
  var currentVersion: String {
    let mainVersion = Bundle.main.infoDictionary!["CFBundleShortVersionString"] as! String

    let codePushVersionPath = diskManager.allCodepushVersions.max { maxVersion(x1: $0, x2: $1) }
    guard let codePushVersion = codePushVersionPath?.components(separatedBy: "/").last(where: { !$0.isEmpty }) else {
      return mainVersion
    }
    
    return [mainVersion, codePushVersion].max(by: { maxVersion(x1: $0, x2: $1) }) ?? mainVersion
  }

  @objc var bundlePath: URL {
    let mainVersion = Bundle.main.infoDictionary!["CFBundleShortVersionString"] as! String
    guard let codePushVersionPath = diskManager.allCodepushVersions.max(by: { maxVersion(x1: $0, x2: $1) }) else {
      return  Bundle.main.url(forResource: "main", withExtension: "jsbundle")!
    }
    
    if maxVersion(x1: mainVersion, x2: codePushVersionPath), let codePushUrl = URL(string: codePushVersionPath) {
      return codePushUrl.appendingPathComponent("main.jsbundle")
    }
    
    return  Bundle.main.url(forResource: "main", withExtension: "jsbundle")!
  }
}

func maxVersion(x1: String, x2: String) -> Bool {
  let version1 = x1.components(separatedBy: "/").last(where: { !$0.isEmpty })!
  let version2 = x2.components(separatedBy: "/").last(where: { !$0.isEmpty })!
  
  return version1.compare(version2, options: .numeric) == .orderedAscending
}
