import cv2
import random


def detect_faces(cascade, image):
    gray_image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

    faces_rect = cascade.detectMultiScale(gray_image)
    # print 0 0 image width/height if no faces
    if len(faces_rect) == 0:
        print(0)
        print(0)
        h, w, c = image.shape
        print(w)
        print(h)
        return
    face = random.randint(0, len(faces_rect) - 1)
    (x, y, w, h) = faces_rect[face]
    print(x)
    print(y)
    print(w)
    print(h)
    return


# loading image
file = "lib/opencv-cat/outFile.jpg"
test_image2 = cv2.imread(file)
haar_cascade_face = cv2.CascadeClassifier("lib/opencv-cat/haarcascade_frontalcatface_extended.xml")
# call the function to detect faces
detect_faces(haar_cascade_face, test_image2)
