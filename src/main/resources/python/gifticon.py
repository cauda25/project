from PIL import Image, ImageDraw, ImageFont
import json
import sys
import os
from datetime import datetime, timedelta
import random

# importing EAN13 from the python-barcode module
from barcode import EAN13

# importing ImageWriter from python-barcode to generate an image file
from barcode.writer import ImageWriter


def run():
    # 표준 입력에서 JSON 데이터 읽기
    data = sys.stdin.read()

    try:
        # JSON 데이터 파싱
        order_data = json.loads(data)
        print(f"파싱 후: {order_data}")

        # 데이터를 반환
        return order_data
    except Exception as e:
        print(f"Error parsing JSON: {e}")
        return None  # 예외가 발생한 경우에도 None을 반환하는 부분


# 이미지 크기 (가로, 세로)
img_width = 716
img_height = 960

# 배경 색상
background_color = (255, 255, 255)  # 흰색

order_info = run()
print(f"변수에 담은 후: {order_info}")
product_info = order_info.get("productDto")
quantity = order_info.get("quantity")

# 이미지 생성
image = Image.new("RGB", (img_width, img_height), background_color)
draw = ImageDraw.Draw(image)

# 폰트 설정 (Malgun Gothic 폰트 사용)
try:
    font = ImageFont.truetype(
        "C:/Windows/Fonts/malgun.ttf", size=24
    )  # 윈도우 시스템에서 Malgun Gothic 폰트 경로
except IOError:
    font = ImageFont.load_default()  # 폰트를 찾을 수 없으면 기본 폰트 사용

# 텍스트 색상
text_color = (0, 0, 0)  # 검정색

# 텍스트를 이미지에 작성
x_offset = 20
y_offset = 20
line_height = 30


def draw_text(text):
    global y_offset
    bbox = draw.textbbox((0, 0), text, font=font)  # (0, 0)은 좌표 시작점
    text_width = bbox[2] - bbox[0]  # 텍스트 너비
    text_height = bbox[3] - bbox[1]  # 텍스트 높이
    x_offset = (img_width - text_width) // 2  # 중앙에 위치시키기 위한 X 좌표
    draw.text((x_offset, y_offset), text, fill=text_color, font=font)
    y_offset += text_height + 10  # 다음 줄로 이동


# 이미지 경로 설정
image_directory = "src/main/resources/static/img"  # 이미지가 저장된 폴더
img_filename = product_info["image"]
# 레이블 + 값 텍스트
name = product_info["name"]
item_details = product_info["itemDetails"]
order_id = str(order_info["orderId"])

# 현재 날짜 구하기
current_date = datetime.now()
# 90일 후 날짜 계산하기
future_date = current_date + timedelta(days=90)

# 바코드 생성
# add any 12 digit number you would like to
number = str(random.randint(100000000000, 999999999999))
# make a class and pass the number with the ImageWriter() as the writer from the variable number created above
my_code = EAN13(number, writer=ImageWriter())
my_code.save(image_directory + "/temp_bar_code")


# 이미지 삽입
try:
    product_image = Image.open(
        image_directory + "/product" + img_filename
    )  # 이미지 파일 열기
    # product_image = product_image.resize((200, 200))  # 크기 조절
    image.paste(
        product_image, ((image.width - product_image.width) // 2, y_offset)
    )  # 배경 이미지에 붙이기
    y_offset += product_image.height + 10  # 이미지 아래로 이동 (이미지 높이 + 여백)
except Exception as e:
    print(f"이미지 로드 실패: {e}")

# 텍스트의 크기 구하기
draw_text(name)
draw_text(item_details)

try:
    bar_code_image = Image.open(
        image_directory + "/temp_bar_code.png"
    )  # 이미지 파일 열기
    image.paste(
        bar_code_image, ((image.width - bar_code_image.width) // 2, y_offset)
    )  # 배경 이미지에 붙이기
    y_offset += bar_code_image.height + 10  # 이미지 아래로 이동 (이미지 높이 + 여백)
except Exception as e:
    print(f"이미지 로드 실패: {e}")
os.remove(image_directory + "/temp_bar_code.png")


draw_text("교환처: CINEPLANET")
draw_text("유효기간: " + future_date.strftime("%Y-%m-%d"))
draw_text("주문번호: " + order_id)


# 이미지 저장
image.save(
    "src/main/resources/static/img/gifticon/"
    + order_id
    + "_"
    + number
    + "_gifticon.png"
)

# 이미지를 화면에 표시
# image.show()
