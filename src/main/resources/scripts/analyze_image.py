import sys
import os
import tensorflow as tf
from tensorflow.keras.preprocessing import image
import numpy as np
import json
import requests

# Python의 표준 출력과 표준 오류를 UTF-8로 설정
sys.stdout.reconfigure(encoding='utf-8')
sys.stderr.reconfigure(encoding='utf-8')

# 이미지 경로를 명령행 인수로 받기
if len(sys.argv) < 2:
    print("이미지 경로를 제공해야 합니다.")
    sys.exit(1)

image_path = sys.argv[1]
spring_url = 'http://localhost:8089/server/api/predictions/save'  # Spring Boot 애플리케이션의 엔드포인트 URL
spring_url2 = 'http://localhost:8089/server/api/predictions/select'

# 현재 작업 디렉토리 출력
print(f"현재 작업 디렉토리: {os.getcwd()}")
print(f"이미지 경로: {image_path}")

# 이미지 경로가 존재하는지 확인
if not os.path.exists(image_path):
    print(f"이미지 파일이 존재하지 않습니다: {image_path}")
    sys.exit(1)

# 모델 경로 설정
model_paths = [
    'src/main/resources/scripts/fish_class_cnn.keras',
    'src/main/resources/scripts/fish_class_dnn.keras',
    'src/main/resources/scripts/fish_class_tl.keras'
]

# 이미지를 분석하는 함수
def analyze_image(image_path):
    # 결과를 저장할 리스트
    results = []
    print("분석 시작")
    # 각 모델에 대해 반복
    for idx, model_path in enumerate(model_paths):
        # 모델 파일 존재 여부 확인
        if not os.path.exists(model_path):
            print(f"모델 파일이 존재하지 않습니다: {model_path}")
            continue

        # TensorFlow 모델 로드
        model = tf.keras.models.load_model(model_path)
        # 이미지 불러오기 및 전처리
        img = image.load_img(image_path, target_size=(150, 150))  # 이미지 크기를 모델에 맞게 조정
        img_array = image.img_to_array(img)
        img_array = np.expand_dims(img_array, axis=0)
        img_array /= 255.

        # 예측 수행
        prediction = model.predict(img_array)

        # 클래스 레이블 가져오기
        classes = ['림포시스티스병', '비브리오', '아가미흡충', '연쇄구균병']  # 클래스 레이블을 적절하게 수정

        # 클래스 별 확률 가져오기
        class_probabilities = {class_name: f"{probability * 100:.2f}" for class_name, probability in zip(classes, prediction[0])}

        # 결과 튜플로 저장하여 리스트에 추가
        result = (f"{['CNN', '전이학습', 'DNN'][idx]} 모델", class_probabilities)
        results.append(result)

        # 현재까지의 모델 분석 상태 출력
        print(f"{idx + 1}/{len(model_paths)} 분석 완료")

    return results
def send_to_spring(result, url, url2, image_path):
    # 데이터를 JSON 형식으로 변환
    prediction_list = [
        {
            "modelName": model_name,
            "imageUrl":image_path,
            "림포시스티스병": probabilities['림포시스티스병'],
            "비브리오": probabilities['비브리오'],
            "아가미흡충": probabilities['아가미흡충'],
            "연쇄구균병": probabilities['연쇄구균병']
        }
        for model_name, probabilities in result
    ]

    # POST 요청을 통해 데이터 전송
    response = requests.post(url, json=prediction_list)
    requests.post(url2, json=prediction_list)

    # 응답 확인
    if response.status_code == 200:
        print("데이터가 성공적으로 전송되었습니다.")
    else:
        print(f"데이터 전송에 실패했습니다. 상태 코드: {response.status_code}")
        print(response.text)
if __name__ == "__main__":
    # 이미지 분석 결과 출력
    result = analyze_image(image_path)
    print(result)
    # 결과를 Spring Boot 엔드포인트로 전송
    send_to_spring(result, spring_url, spring_url2, image_path)
