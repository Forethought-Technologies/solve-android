package ai.forethought.kustomer

/**
 * When Kustomer has an open or unread conversation,
 * choose what you would like to happen when the user asks for support
 */
enum class KustomerResumeConversationSetting {
    /**
     * Even if there is an open or unread message, always launch Forethought
     */
    ALWAYS_START_NEW_CONVERSATION,

    /**
     * Default. If there's an unread message from an agent, show it.
     * If there's an open conversation, prompt them.
     */
    SHOW_FOR_UNREAD_MESSAGE_PROMPT_FOR_OPEN,

    /**
     * If there's an open conversation, show Kustomer
     */
    SHOW_FOR_OPEN_CONVERSATION,

    /**
     * If there's an unread or an open conversation, prompt the user to select
     */
    PROMPT_IF_OPEN_OR_UNREAD
}
