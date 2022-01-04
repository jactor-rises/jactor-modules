package com.github.jactor.persistence.entity

import javax.persistence.Embeddable
import javax.persistence.Lob
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import java.util.Objects

@Embeddable
class EntryEmbeddable {
    var creatorName: String? = null
        private set

    @Lob
    var entry: String? = null
        private set

    constructor()

    /**
     * @param entryEmbeddable is the entry to create a copyWithoutId of
     */
    private constructor(entryEmbeddable: EntryEmbeddable) {
        creatorName = entryEmbeddable.creatorName
        entry = entryEmbeddable.entry
    }

    internal constructor(creatorName: String?, entry: String?) {
        this.creatorName = creatorName
        this.entry = entry
    }

    fun copy() = EntryEmbeddable(this)
    fun fetchCreatorName() = creatorName ?: throw IllegalStateException("No creatorName provided for $this")
    fun fetchEntry() = entry ?: throw IllegalStateException("No entry in $this")

    fun modify(modifiedCreator: String?, modifiedEntry: String?) {
        creatorName = modifiedCreator
        entry = modifiedEntry
    }

    override fun equals(other: Any?): Boolean {
        return this === other || other != null && javaClass == other.javaClass && isEqualTo(other as EntryEmbeddable)
    }

    private fun isEqualTo(obj: EntryEmbeddable): Boolean {
        return entry == obj.entry &&
                creatorName == obj.creatorName
    }

    override fun toString(): String {
        return ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(creatorName).append(shortEntry()).toString()
    }

    private fun shortEntry(): String? {
        return if (entry == null || entry!!.length < 50) {
            entry
        } else entry!!.substring(0, 47) + "..."
    }

    override fun hashCode(): Int {
        return Objects.hash(creatorName, entry)
    }
}