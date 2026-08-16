/**
 * Every request that creates/updates/deletes data, or accesses a
 * role-gated endpoint, should identify who's doing it and what role
 * they have. Merge this into your fetch() headers.
 *
 * actorOverride lets a page specify the acting user explicitly (e.g. the
 * Registration or Login pages, where nobody is logged in yet). Otherwise
 * it falls back to whoever is logged in, per localStorage.
 */
function withAuditHeader(extraHeaders = {}, actorOverride = null) {
    return {
        "X-User-Email": actorOverride || localStorage.getItem("userEmail") || "SYSTEM",
        "X-User-Role": localStorage.getItem("role") || "",
        ...extraHeaders
    };
}