/**
 * Factory class for creating different types of Renderers.
 * Implements the Factory design pattern to The Single Responsibility Principle.
 *
 * Supported renderer types:
 * - console: that renders the board to the console.
 * - void: that performs no rendering.
 *
 * @author Amit Moses
 */
public class RendererFactory {

    /**
     * Initializes a new RendererFactory.
     */
    public RendererFactory(){}

    /**
     * Creates a renderer instance based on the specified type.
     *
     * @param type String identifier for renderer type:
     *             "console" - Console renderer
     *             "void" - Void renderer
     * @param size the size of the board
     * @return Renderer instance of requested type, or null if type is invalid
     */
    public Renderer buildRenderer(String type, int size){
        return switch (type) {
            case "console" -> new ConsoleRenderer(size);
            case "void" -> new VoidRenderer();
            default -> null;
        };
    }
}
