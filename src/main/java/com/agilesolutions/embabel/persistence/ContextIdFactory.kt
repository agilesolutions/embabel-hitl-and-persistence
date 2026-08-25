package com.agilesolutions.embabel.persistence

import com.embabel.agent.core.ContextId

object ContextIdFactory {

    @JvmStatic
    fun create(value: String): ContextId =
        ContextId(value)
}