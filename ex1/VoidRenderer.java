/**
 * VoidRenderer performs no rendering.
 * It implements the Renderer interface but provides no visual output.
 *
 * @author Amit Moses
 */
public class VoidRenderer implements Renderer {
    /**
     * Initializes the VoidRenderer.
     */
    public VoidRenderer(){}

    /**
     * Does nothing when called.
     *
     * @param board the board to render (not used)
     */
    @Override
    public void renderBoard(Board board){}
}