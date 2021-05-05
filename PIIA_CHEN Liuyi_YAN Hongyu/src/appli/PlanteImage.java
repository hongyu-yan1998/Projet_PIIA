package appli;

import javafx.scene.image.ImageView;

public class PlanteImage {
	private ImageView image;
	
	public PlanteImage(ImageView image) {
		this.image = image;
	}

	public void setImage(ImageView image) {
        this.image = image;
    }

    public ImageView getImage() {
        return image;
    }
}
