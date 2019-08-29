class Point2D {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }
    setLocation(x, y) {
        if (typeof x == Point2D) {
            y = x.y;
            x = x.x;
        }
        this.x = x;
        this.y = y;
        return this;
    }
    toString() {
        return "Point2D[x:" + this.x + ", y:" + this.y + "]";
    }
}
class Point extends Point2D {
    constructor(x, y) {
        super(0, 0);
        this.setLocation(x, y);
    }
    setLocation(x, y) {
        if (typeof x === Point2D) {
            y = x.y;
            x = x.x;
        }
        this.x = Math.floor(x);
        this.y = Math.floor(y);
        return this;
    }
}
class Ray {
    constructor(x, y, ang, dist) {
        if (dist === undefined)
            dist = 10;
        this.loc = new Point2D(x, y);
        this.ang = ang;
        this.dist = dist;
    }
    toString() {
        return "Ray[loc:" + this.loc + ", ang:" + this.ang + ", dist:" + this.dist + "]";
    }
}

function toDegrees(angle) {
    return angle * (180 / Math.PI);
}

function castRay(ray, verbose) {
    if (verbose === undefined) verbose = false;

    // start prepping vars
    //    Origin stuff
    let origin = ray.loc;
    let originTile = new Point(origin.x, origin.y);
    //    Moving stuff
    let tile = new Point(ray.loc.x, ray.loc.y);
    let mvp = new Point2D(ray.loc.x, ray.loc.y); // mvp stands for "MoVing Point". ... ... ... or Most Valuable Point. ;)
    let dirX, dirY;
    let dist;
    //    Function stuff
    let m, f, g;
    //    Loop stuff
    let hit = false;
    let exhausted = false;

    //get gradiant and produce functions
    m = -Math.tan(toDegrees(ray.ang+ 90));
    f = (x) => -m * (x - origin.x) + origin.y;
    g = (y) => (1 / m) * (y - origin.y) + origin.x;

    // determine direction to traverse
    /* hidden for a pre finished test...
    dirX = ray.ang > 0 && ray.ang < 180 ? 1 : ray.ang == 0 || ray.ang == 180 ? 0 : -1;
    dirY = ray.ang > 90 && ray.ang < 270 ? 1 : ray.ang == 90 || ray.ang == 270 ? 0 : -1; // funny, negative is upwards!
    */
    //    determined that we won't need to minus anything
    dirX = ray.ang > 0 && ray.ang < 180 ? 1 : 0;
    dirY = ray.ang > 90 && ray.ang < 270 ? 1 : 0;

    // -*-*-*-*-*-*-*-* put skip loop here *-*-*-*-*-*-*-*-

    /*


WHAT IF WE:
- PRE CALCULATE ALL THE COORDINATES
- SORT THEM BY DISTANCE FROM ORIGIN
- TEST COLLISIONS

double the time to complete, though...
rn, we doing like a loop and a half, whereas the above would take three loops, if not more...


    */




    // quik check to see if we've hit at our origin
    if (isSolid(origin.x, origin.y)) {
        ray.dist = 0;
        return ray;
    }

    // start moving!
    do {
        // move tile
        tile.setLocation(mvp.x + dirX, mvp.y + dirY);

        // get next whole x and y
        mvp.x = g(tile.y);
        mvp.y = f(tile.x);

        // convert it to closest point
        if (verbose) console.log("points:  orig:" + origin);
        if (verbose) console.log("points:  tile:" + tile);
        if (verbose) console.log("points:  mvpx:" + new Point2D(mvp.x, tile.y) + ", mvpy" + new Point2D(tile.x, mvp.y));
        if (verbose) console.log("compare: " + distance(mvp.x, tile.y, origin.x, origin.y) + ", " + distance(tile.x, mvp.y, origin.x, origin.y));
        if (distance(mvp.x, tile.y, origin.x, origin.y) < distance(tile.x, mvp.y, origin.x, origin.y)) mvp.y = tile.y;
        else mvp.x = tile.x;

        // check to see if we are exhausted
        if ((dist = distance(mvp.x, mvp.y, origin.x, origin.y)) >= ray.dist) exhausted = true;
        else if (!(mvp.x >= 0 && mvp.x < mapWidth && mvp.y >= 0 && mvp.y < mapHeight)) exhausted = true;

        // determine if hit is solid
        if (!exhausted && isSolid(mvp.x, mvp.y)) {
            hit = true;
            ray.dist = dist;
            if (verbose) console.log("HIT! " + new Point(mvp.x, mvp.y));
        }

        // log debug output
        if (verbose) console.log("MVP: [x:" + mvp.x + ", y:" + mvp.y + "], dist:" + dist + ", hit:" + hit + ", exhausted:" + exhausted + "\n");
    } while (!hit && !exhausted);

    return ray;
}

function distance(x1, y1, x2, y2) {
    let x = x2 - x1;
    let y = y2 - y1;
    return Math.sqrt(x * x + y * y);
}

function isSolid(x, y) {
    x = Math.floor(x);
    y = Math.floor(y);
    return map[y][x] > 0;
}


// GUTS OF THE GAME!
// implement this in the game, and then draw a dot on the casted ray...

console.log("\n\nRaycast testing!\n\n");

let map = [
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 1, 1, 1, 0, 0, 1, 1, 0],
    [0, 0, 1, 0, 0, 0, 0, 0, 1, 0],
    [0, 0, 1, 0, 0, 0, 0, 0, 1, 0],
    [0, 0, 1, 0, 0, 0, 0, 0, 1, 0],
    [0, 0, 1, 0, 0, 0, 0, 0, 1, 0],
    [0, 0, 1, 0, 0, 0, 0, 0, 1, 0],
    [0, 0, 1, 1, 1, 1, 1, 1, 1, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
];

let mapHeight = map.length;
let mapWidth = map[0].length;

let player = new Point2D(3.75, 2.25);
console.log("CASTING RAY: " + castRay(new Ray(player.x, player.y, 110, 10), true));

//player.setLocation(4.5, 4.5);
//console.log("CASTING ANOTHER RAY: " + castRay(new Ray(player.x, player.y, 110, 10), true));