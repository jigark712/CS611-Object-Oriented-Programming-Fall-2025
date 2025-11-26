package legends.world;

public abstract class Tile {

    protected final TileType type;

    protected Tile(TileType type) {
        this.type = type;
    }

    public TileType getType() {
        return type;
    }

    public boolean isAccessible() {
        return type != TileType.INACCESSIBLE;
    }

    public boolean isMarket() {
        return type == TileType.MARKET;
    }
}
