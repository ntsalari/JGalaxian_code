package jgalaxian.Logic;

public class Bbox {

    private int x, y;
    private int width, heigth;

	public Bbox(){
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.heigth = 0;
	}
    public Bbox(int x, int y, int width, int heigth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.heigth = heigth;
    }

    public boolean inBound(Bbox enemy) {
        if (this.x < enemy.getEndX() && this.getEndX() > enemy.getX() && this.y < enemy.getEndY() && this.getEndY() > enemy.y) {
            return true;
        }

        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setWidth(int width){
		this.width = width;
	}
	
	public void setHeigth(int heigth){
		this.heigth = heigth;
	}
	
	public int[] getBounds(){
		int[] bounds;
		bounds = new int[6];
		bounds[0] = x;
		bounds[1] = y;
		bounds[2] = getEndX();
		bounds[3] = getEndY();
		bounds[4] = width;
		bounds[5] = heigth;
		return bounds;
	}

    public int getEndX() {
        return x + width;
    }

    public int getEndY() {
        return y + heigth;
    }

    public int getWidth() {
        return width;
    }

    public int getHeigth() {
        return heigth;
    }
}
