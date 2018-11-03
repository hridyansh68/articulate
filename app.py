from flask import Flask, request, jsonify, send_file
from textrank import extract_key_phrases
import nltk
from PIL import Image
from resizeimage import resizeimage
from google_images_download import google_images_download
from nltk.corpus import stopwords
import os
import ffmpeg
import subprocess
from PIL import Image
from requests import post
from flask_restful import Resource, Api, reqparse
import operator


app = Flask(__name__)
api = Api(app)


def freqCount(text):
    tokens = [t for t in text.split()]
    clean_tokens = tokens[:]
    sr = stopwords.words('english')
    for token in tokens:
        if token in stopwords.words('english'):
            clean_tokens.remove(token)
        freq = nltk.FreqDist(clean_tokens)
        sorted_list = sorted(
            freq.items(), key=operator.itemgetter(1), reverse=True)
    max1 = sorted_list[0][0]
    max2 = sorted_list[1][0]
    return max1, max2


def getPhrases(text):
    list = extract_key_phrases(text)
    return list


def getImages(fcount, phrases):
    s=''
    for string in phrases:
        s =s+ fcount[0]+' '+fcount[1]+' '+string+','
    print(s)
    response = google_images_download.googleimagesdownload()
    absolute_image_paths = response.download(
        {'keywords': s, 'limit': 1, 'no_directory': True, 'size': 'medium', 'format': 'jpg', 'no_numbering': True})
    return True


def renameFiles():
    dirname = os.path.dirname(__file__)
    dir_path = os.path.join(dirname, 'downloads')
    files = os.listdir(dir_path)
    i = 0
    for file in files:
        v_image = Image.open(os.path.join(dir_path, file))
        try:
            v_image.verify()
            name = str(i)
            if i < 10:
                name = '0'+name
            os.rename(os.path.join(dir_path, file),
                      os.path.join(dir_path, name+'.jpg'))
            i = i+1
        except Exception:
            pass
    return True


def generateVideo():
    os.system(
        "ffmpeg -f image2 -r 1/2 -i downloads/%02d.jpg -vcodec mpeg4 -y out.mp4")
    return True


class Articulate(Resource):
    def post(self):
        try:
            os.rmdir('downloads')
        except OSError:
            pass
        json_data = request.get_json(force=True)
        text = json_data['content']
        fcount = freqCount(text)
        phrases = getPhrases(text)
        getImages(fcount, phrases)
        renameFiles()
        generateVideo()
        return send_file('out.mp4', attachment_filename='output.mp4')


api.add_resource(Articulate, '/')

if __name__ == "__main__":
    # app.run(host='0.0.0.0',port='8080')
    app.run(debug=True)
