from flask import Flask, request , jsonify
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
from flask_restful import Resource, Api , reqparse
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
        sorted_list = sorted(freq.items(), key=operator.itemgetter(1),reverse=True)
    max1 = sorted_list[0][0]
    max2 = sorted_list[1][0]
    return max1,max2

class Articulate(Resource):
    def post(self):
        json_data = request.get_json(force=True)
        text = json_data['content']
        a=freqCount(text)
        print(a)
        return {
            'status': True
        }
    

api.add_resource(Articulate, '/')

if __name__ == "__main__":
    #app.run(host='0.0.0.0',port='8080')
    app.run(debug=True)