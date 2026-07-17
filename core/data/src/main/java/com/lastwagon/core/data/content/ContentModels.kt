package com.lastwagon.core.data.content

import com.lastwagon.core.model.ApplicabilityFlag
import com.lastwagon.core.model.VerificationStatus

/**
 * Authored, source-verified inspection content parsed from the versioned JSON asset. This is the
 * production content model (distinct from the labeled `SampleContent`). Nothing here ships unless
 * it passes [ContentValidator]; the "no non-VERIFIED item ships" gate is enforced there.
 */
data class InspectionContent(
    val schemaVersion: Int,
    val contentVersion: String,
    val verifiedOn: String,
    val expectedItemCount: Int,
    val categories: List<ContentCategory>,
    val items: List<ContentItem>,
)

data class ContentCategory(val id: String, val title: String, val sequence: Int)

data class ContentItem(
    val id: String,
    val categoryId: String,
    val sequence: Int,
    val name: String,
    val inspectFor: String,
    val defects: List<String>,
    val applicability: Set<ApplicabilityFlag>,
    val citations: List<Citation>,
    val verificationStatus: VerificationStatus,
    val verifiedOn: String?,
    val notes: String?,
)

/** Structured provenance: a factual reference to a public source (or a manual cross-reference). */
data class Citation(val type: CitationType, val ref: String, val url: String?)

/** Citation source classes. CFR/FMVSS/FMCSA_GUIDE are public-domain and quotable; MANUAL and
 *  STATE_MANUAL are copyright-restricted cross-references only (fact verification, never text). */
enum class CitationType { CFR, FMVSS, FMCSA_GUIDE, STATE_MANUAL, MANUAL }
