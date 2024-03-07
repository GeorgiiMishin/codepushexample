import Foundation
import ZIPFoundation

class DiskManager {
  static let shared = DiskManager()
  
  private let bundlesPath: URL
  private let archivesPath: URL
  
  private var allVersions: [String] = []
  
  var bundles: URL {
    return bundlesPath
  }
  
  var archives: URL {
    return archivesPath
  }
  
  var allCodepushVersions: [String] {
    return allVersions
  }
  
  init() {
    guard let documentDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first else {
      fatalError("Documents directory not found")
    }
    
    let bundlesPath = documentDirectory.appendingPathComponent("bundles")
    let archivesPath = documentDirectory.appendingPathComponent("archives")
    
    if !FileManager.default.fileExists(atPath: bundlesPath.path) {
      try! FileManager.default.createDirectory(at: bundlesPath, withIntermediateDirectories: false)
    }
    
    if !FileManager.default.fileExists(atPath: archivesPath.path) {
      try! FileManager.default.createDirectory(at: archivesPath, withIntermediateDirectories: false)
    }
    
    self.bundlesPath = bundlesPath
    self.archivesPath = archivesPath
    self.allVersions = getAllVersions()
  }
  
  func unpack(archive: URL) throws {
    try FileManager.default.unzipItem(at: archive, to: bundles)
    try FileManager.default.removeItem(at: archive)
  }
  
  private func getAllVersions() -> [String] {
    if let allBundles = try? FileManager.default.contentsOfDirectory(at: bundlesPath, includingPropertiesForKeys: [.isDirectoryKey], options: [.skipsHiddenFiles]) {
      return allBundles.map { $0.absoluteString }
    }
    
    return []
  }
}
