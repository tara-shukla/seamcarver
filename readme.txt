Programming Assignment 7: Seam Carving


/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */
We implemented a version of Bellman-Ford's algorithm to find the shortest vertical
path down the picture. We iterate through all the pixels, only relaxing adjacent if
the distance calculated to it is a minimum.


/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */

The seam-carving approach works on images that



/* *****************************************************************************
 *  Perform computational experiments to estimate the running time to reduce
 *  a W-by-H image by one column and one row (i.e., one call each to
 *  findVerticalSeam(), removeVerticalSeam(), findHorizontalSeam(), and
 *  removeHorizontalSeam()). Use a "doubling" hypothesis, where you
 *  successively increase either W or H by a constant multiplicative
 *  factor (not necessarily 2).
 *
 *  To do so, fill in the two tables below. Each table must have 5-10
 *  data points, ranging in time from around 0.25 seconds for the smallest
 *  data point to around 30 seconds for the largest one.
 **************************************************************************** */

(keep W constant)
 W = 2000
 multiplicative factor (for H) = 3

 H           time (seconds)      ratio       log ratio
------------------------------------------------------
300             0.236                         0
900             0.6              1.27         0.218
2700            1.673            2.79         0.934
8100            5.269            3.15         1.044
24300           32.785           6.22         1.664



(keep H constant)
 H = 2000
 multiplicative factor (for W) = 3

 W           time (seconds)      ratio       log ratio
------------------------------------------------------
300            0.245                            0
900            0.57               2.33          0.779
2700           1.646              2.89          0.966
8100           6.077              3.69          1.188
24300          34.464             5.67          1.579


/* *****************************************************************************
 *  Using the empirical data from the above two tables, give a formula
 *  (using tilde notation) for the running time (in seconds) as a function
 *  of both W and H, such as
 *
 *       ~ 5.3*10^-8 * W^5.1 * H^1.5
 *
 *  Briefly explain how you determined the formula for the running time.
 *  Recall that with tilde notation, you include both the coefficient
 *  and exponents of the leading term (but not lower-order terms).
 *  Round each coefficient and exponent to two significant digits.
 **************************************************************************** */
To calculate the exponents of W and H, we calculated the average log ratio using
the data we collected above. Then, we plugged into the equation using one sample
to find the coefficient.

Running time (in seconds) to find and remove one horizontal seam and one
vertical seam, as a function of both W and H:


    ~ a * W^ H^0.97
       _______________________________________




/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
N/A

/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 **************************************************************************** */
N/A

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
N/A

/* *****************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */
Noelle: implemented energy() and transposing the picture; helped code and debug other implementations
Tara: implemented shortPath() to find the seams; helped code and debug other implementations

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
N/A
