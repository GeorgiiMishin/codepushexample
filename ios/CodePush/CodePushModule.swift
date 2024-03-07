import Foundation

@objc(CodePushModule)
class CodePushModule: NSObject {
  private let downloadManager = DownloadManager.shared
  private let codePushManager = CodePushManager.shared
  private let diskManager = DiskManager.shared

  @objc func initialize(_ host: String) {
    #if DEBUG
      return
    #endif
    
    downloadManager.downloadBundle(host: host, currentVersion: codePushManager.currentVersion) { error, path in
      if let path {
        try? DiskManager.shared.unpack(archive: path)
      }
      
      if let error {
        // Handle error
      }
    }
  }
  
  @objc
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
}
