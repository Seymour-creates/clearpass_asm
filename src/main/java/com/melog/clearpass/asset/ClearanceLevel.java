package com.melog.clearpass.asset;

/**
 * Clearance categories supported by ClearPass.
 *
 * Feel free to add or rename levels to match your real policy; only
 * the enum names and order matter to code.
 */
public enum ClearanceLevel {
    /** Ordinary unclassified material (no marking). */
    UNCLASSIFIED,

    /** Sensitive but unclassified—for official-use-only documents. */
    FOUO,

    /** U.S. CONFIDENTIAL information. */
    CONFIDENTIAL,

    /** U.S. SECRET information. */
    SECRET,

    /** U.S. TOP SECRET information. */
    TOP_SECRET
}
