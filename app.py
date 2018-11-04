from flask import Flask, request, jsonify, send_from_directory
from textrank import extract_key_phrases
import nltk
from PIL import Image
from resizeimage import resizeimage
from google_images_download import google_images_download
from nltk.corpus import stopwords
import os,shutil
import ffmpeg
import subprocess
from PIL import Image
from requests import post
from flask_cors import CORS
from textblob import TextBlob
from flask_restful import Resource, Api, reqparse
import operator


app = Flask(__name__)
api = Api(app)

CORS(app)

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
    list = []
    for listitem in sorted_list:
        list.append(listitem[0])
    taglist = nltk.tag.pos_tag(list)
    finallist = []
    finallist.append(taglist[0][0])
    finallist.append(taglist[1][0])
    i=2
    while i<len(taglist) and len(finallist)<=4:
        if taglist[i][1]=='NN':
            finallist.append(taglist[i][0])
        i=i+1

    return finallist


def getPhrases(text):
    list = extract_key_phrases(text)
    return list


def getImages(fcount, phrases):
    # dirname = os.path.dirname(__file__)
    # dir_path = os.path.join(dirname, 'downloads')
    # for file in os.listdir(dir_path):
    #     file_path = os.path.join(dir_path,file)
    #     try:
    #         if os.path.isfile(file_path):
    #             os.remove(file)
    #     except Exception as e:
    #         print(e)
    s=''
    for string in phrases:
        s =s+ fcount[0]+' '+fcount[1]+' '+string+','
    s=s[:-1]
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
        try:
            v_image = Image.open(os.path.join(dir_path, file))
            v_image.verify()
            name = str(i)
            if i < 10:
                name = '0'+name
            os.rename(os.path.join(dir_path, file),
                      os.path.join(dir_path, name+'.jpg'))
            i = i+1
        except Exception:
            pass
    return dirname


def generateVideo(senti):
    sentimental = senti
    print(sentimental)
    if os.path.exists("output.mp4"):
        os.remove("output.mp4")
    
    if sentimental>=-1 and sentimental<=-0.5:
        os.system("ffmpeg -f image2 -r 1/2 -i downloads/%02d.jpg -i s1.mp3 -vcodec mpeg4 -y out.mp4")
    elif sentimental>=-1 and sentimental<=-0.5:
        os.system("ffmpeg -f image2 -r 1/2 -i downloads/%02d.jpg -i s2.mp3 -vcodec mpeg4 -y out.mp4")
    elif sentimental>=-1 and sentimental<=-0.5:
        os.system("ffmpeg -f image2 -r 1/2 -i downloads/%02d.jpg -i s3.mp3 -vcodec mpeg4 -y out.mp4")
    else:
        os.system("ffmpeg -f image2 -r 1/2 -i downloads/%02d.jpg -i s4.mp3 -vcodec mpeg4 -y out.mp4")
    
    return True


class Articulate(Resource):
    def post(self):
        os.system("rm -rf downloads")
        os.system("rm -rf out.mp4")
        json_data = request.get_json(force=True)
        text = json_data['content']
        text = text.lower()
        testimonial = TextBlob(text)
        sentimentalval = testimonial.sentiment[0]
        fcount = freqCount(text)
        phrases = getPhrases(text)
        getImages(fcount, phrases)
        path=renameFiles()
        generateVideo(sentimentalval)
        return send_from_directory(path,'out.mp4')


api.add_resource(Articulate, '/')

if __name__ == "__main__":
    app.run(host='0.0.0.0',port='8080')
    #app.run(debug=True)
