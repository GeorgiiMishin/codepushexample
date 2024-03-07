import Foundation

class DownloadManager {
  static let shared = DownloadManager()
  
  private let diskManager = DiskManager.shared
  
  func downloadBundle(
    host: String,
    currentVersion: String,
    _ completion: @escaping (_ error: Error?, _ path: URL?) -> Void
  ) {
    guard let url = URL(string: "\(host)/ios/\(currentVersion)") else {
      return
    }
    
    var request = URLRequest(url: url)
    request.httpMethod = "GET"
    
    let archivePath = diskManager.archives.appendingPathComponent("\(UUID().uuidString).zip")
    
    URLSession.shared.dataTask(with: request) { data, response, error in
      if error == nil, let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200, let data {
        do {
          try data.write(to: archivePath)
          completion(nil, archivePath)
        } catch {
          completion(error, archivePath)
        }
      }
    }.resume()
  }
}
