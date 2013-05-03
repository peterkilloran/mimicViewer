package org.pvk.mimic.client;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class LabSelectionUpdatedEventBus extends Event<LabSelectionUpdatedEventBus.Handler> {
	/**
     * Implemented by methods that handle MessageReceivedEvent events.
     */
    public interface Handler {
        /**
         * Called when an {@link MessageReceivedEvent} event is fired.
         * The name of this method is whatever you want it.
         *
         * @param event an {@link MessageReceivedEvent} instance
         */
        void onLabSelectionUpdated(LabSelectionUpdatedEventBus event);
    }

    private static final Type<LabSelectionUpdatedEventBus.Handler> TYPE =
        new Type<LabSelectionUpdatedEventBus.Handler>();

    /**
     * Register a handler for MessageReceivedEvent events on the eventbus.
     * 
     * @param eventBus the {@link EventBus}
     * @param handler an {@link MessageReceivedEvent.Handler} instance
     * @return an {@link HandlerRegistration} instance
     */
    public static HandlerRegistration register(EventBus eventBus,
    		LabSelectionUpdatedEventBus.Handler handler) {
      return eventBus.addHandler(TYPE, handler);
    }    

    private final String message;

    public LabSelectionUpdatedEventBus(String message) {
        this.message = message;
    }

    @Override
    public Type<LabSelectionUpdatedEventBus.Handler> getAssociatedType() {
        return TYPE;
    }

    public String getMessage() {
        return message;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onLabSelectionUpdated(this);
    }

}
