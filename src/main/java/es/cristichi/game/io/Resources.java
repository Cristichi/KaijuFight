package es.cristichi.game.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Resources {
    private static final HashMap<String, BufferedImage> loadedImgs = new HashMap<>(50);
    
    /**
     * Loads a {@link BufferedImage} from the resources.
     * @param resourcePath Path inside the resources folder.
     * @return The image.
     * @throws ResourceException If the image does not exist.
     */
    public static BufferedImage loadImage(String resourcePath) throws ResourceException {
        if (loadedImgs.containsKey(resourcePath)){
            return loadedImgs.get(resourcePath);
        }
        try (InputStream in = Resources.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null){
                throw new NullPointerException("Resource not found.");
            }
            BufferedImage img = ImageIO.read(in);
            loadedImgs.put(resourcePath, img);
            return img;
        } catch (IOException | NullPointerException e) {
            throw new ResourceException(
                    "Resource not found at \"%s\". Cristichi or otherwise the modder probably forgot to add it."
                            .formatted(resourcePath), e);
        }
    }
}
