package ai.forethought.intercom

/**
 * When Intercom has an open or unread conversation,
 * choose what you would like to happen when the user asks for support
 */

enum class IntercomResumeConversationSetting {
    /**
     * Default. Even if there is an open or unread message, always launch Forethought
     */
    ALWAYS_START_NEW_CONVERSATION,
    /**
     * If there's an unread message from an agent, show it.
     * If there's an open conversation, prompt them
     */
    SHOW_FOR_UNREAD_MESSAGE
}