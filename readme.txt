Programming Assignment 7: Seam Carving


/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */
The Seam Carver algorithm employs dynamic programming to find and remove
low-energy seams in images, thereby minimizing visual disruptions. It begins
by calculating the energy of each pixel, which indicates its visual significance.
 Two arrays, distTo and edgeTo, are initialized for tracking the minimum
 cumulative energy and the path of each pixel, respectively. The algorithm
 iterates through the image—row-wise for vertical seams and column-wise for
 horizontal seams—updating these arrays based on the energies of possible
 connecting pixels. The optimal seam is then identified from the end of the
 image, and its path is traced back using the edgeTo array. This seam,
 represented as an array of pixel indices, is the least noticeable and can be
 removed with minimal impact on the image's appearance.

/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */
Seam carving is most effective on images with large areas of low detail such
as skies or bodies of water, allowing seams to be removed without noticeably
altering the image’s content. Conversely, images densely packed with important
details like group photos or intricate patterns are not suitable for seam
carving, as removing seams could distort vital features and introduce visual
artifacts.

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
 multiplicative factor (for H) = 1.5x

 H           time (seconds)      ratio       log ratio
------------------------------------------------------
2000        0.25                -           -
3000        0.60                2.4         1.26
4500        1.35                2.25        1.17
6750        3.05                2.26        1.18
10125       6.87                2.25        1.17
...


(keep H constant)
 H = 2000
 multiplicative factor (for W) = 1.5x

 W           time (seconds)      ratio       log ratio
------------------------------------------------------
2000        0.26                -           -
3000        0.62                2.38        1.25
4500        1.48                2.39        1.25
6750        3.54                2.39        1.25
10125       8.45                2.39        1.25
...



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


Running time (in seconds) to find and remove one horizontal seam and one
vertical seam, as a function of both W and H:


    ~   5.3*10^-8 * W^5.1 * H^1.5
       _______________________________________




/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
N/A



/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
N/A



/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
Very practical assignment, loved it!
