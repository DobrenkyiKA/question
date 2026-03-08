package com.kdob.piq.question.infrastructure.web

import com.kdob.piq.question.infrastructure.client.StorageServiceClient
import com.kdob.piq.question.infrastructure.sync.CatalogSyncService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/sync")
class AdminSyncController(
    private val syncService: CatalogSyncService,
    private val storageClient: StorageServiceClient
) {

    @GetMapping("/versions")
    fun getVersions(): List<String> {
        return storageClient.getVersions()
    }

    @PostMapping("/export")
    fun export(@RequestParam version: String, @RequestParam message: String) {
        syncService.exportToVersion(version, message)
    }

    @PostMapping("/import")
    fun import(@RequestParam version: String) {
        syncService.importFromVersion(version)
    }

    @PostMapping("/import-artifact")
    fun importArtifact(@RequestParam version: String, @RequestBody artifactYaml: String) {
        syncService.importArtifact(version, artifactYaml)
    }
}
