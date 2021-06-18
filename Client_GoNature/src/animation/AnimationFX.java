package animation;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * @author Loic Sculier aka typhon0
 */
public abstract class AnimationFX {

    /**
     * Used to specify an animation that repeats indefinitely, until the
     * {@code stop()} method is called.
     */
    public static final int INDEFINITE = -1;
    private Timeline timeline;
    private boolean reset;
    private Node node;
    private AnimationFX nextAnimation;
    private boolean hasNextAnimation;

    /**
     * Create a new animation
     *
     * @param node the node to affect
     */
    public AnimationFX(Node node) {
        super();
        setNode(node);

    }

    /**
     * Default constructor
     */
    public AnimationFX() {
        hasNextAnimation = false;
        this.reset = false;


    }

    /**
     * Handle when the animation is finished
     *
     * @return
     */
    private AnimationFX onFinished() {
        if (reset) {
            resetNode();
        }
        if (this.nextAnimation != null) {
            this.nextAnimation.play();
        }
        return this;
    }

    /**
     * Set the next animation to play
     *
     * @param animation animation
     * @return aninamtion
     */
    public AnimationFX playOnFinished(AnimationFX animation) {
        setNextAnimation(animation);
        return this;

    }

    /**
     * Function to reset the node or not when the animation is finished
     *
     * @param reset reset
     * @return reset
     */
    public AnimationFX setResetOnFinished(boolean reset) {
        this.reset = reset;
        return this;
    }

    /**
     * Play the animation
     */
    public void play() {
        timeline.play();
    }

    /**
     * Stop the animation
     *
     * @return animation
     */
    public AnimationFX stop() {
        timeline.stop();
        return this;
    }

    /**
     * Function the reset the node to original state
     *
     * @return animation
     */
    abstract AnimationFX resetNode();

    /**
     * Function to initialize the timeline
     */
    abstract void initTimeline();


    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public boolean isResetOnFinished() {
        return reset;
    }

    protected void setReset(boolean reset) {
        this.reset = reset;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
        initTimeline();
        timeline.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(Animation.Status.STOPPED)) {
                onFinished();
            }

        });
    }

    public AnimationFX getNextAnimation() {
        return nextAnimation;
    }

    protected void setNextAnimation(AnimationFX nextAnimation) {
        hasNextAnimation = true;
        this.nextAnimation = nextAnimation;
    }

    public boolean hasNextAnimation() {
        return hasNextAnimation;
    }

    protected void setHasNextAnimation(boolean hasNextAnimation) {
        this.hasNextAnimation = hasNextAnimation;
    }

    /**
     * Define the number of cycles in this animation
     *
     * @param value value
     * @return value
     */
    public AnimationFX setCycleCount(int value) {
        this.timeline.setCycleCount(value);
        return this;
    }

    /**
     * Set the speed factor of the animation
     *
     * @param value value
     * @return value
     */
    public AnimationFX setSpeed(double value) {
        this.timeline.setRate(value);
        return this;
    }

    /**
     * Delays the start of an animation
     *
     * @param value value
     * @return value
     */
    public AnimationFX setDelay(Duration value) {
        this.timeline.setDelay(value);
        return this;
    }

    /**
     * Set event when the animation ended.
     *
     * @param value value
     */
    public final void setOnFinished(EventHandler<ActionEvent> value) {
        this.timeline.setOnFinished(value);
    }

}