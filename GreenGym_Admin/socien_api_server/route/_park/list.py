#list.py -> 사용자 위치를 기반으로 1km 내의 공원리스트를 돌려주는 api
from flask_restx import Resource, reqparse
from sqlalchemy.sql.elements import Null
from .park import Park      
import app
from sqlalchemy import text 
from math import sin, cos, sqrt, atan2, radians #거리계산 함수를 이용

parser = reqparse.RequestParser() 
parser.add_argument('x', help='경도', type=float, required=True) 
parser.add_argument('y', help='위도', type=float, required=True)
parser.add_argument('dist', help='거리', type=float, required=True, default=1) #dist 기본값 = 1

#https://newbedev.com/haversine-formula-in-python-bearing-and-distance-between-two-gps-points <-출처(havrsine공식)
#distLL : 사용자의 위치를 받고(x1, y1) 공원의 위치(x2, y2)와의 거리 계산
def distLL(x1, y1, x2, y2): 
    R = 6373.0 # km
    lat1 = radians(y1) 
    lon1 = radians(x1) 
    lat2 = radians(y2) 
    lon2 = radians(x2) 
    dlon = lon2 - lon1 
    dlat = lat2 - lat1 
    a = sin(dlat / 2)**2 + cos(lat1) * cos(lat2) * sin(dlon / 2)**2 
    c = 2 * atan2(sqrt(a), sqrt(1 - a)) 
    distance = R * c

    return distance 

@Park.route('/list')  
class ParkList(Resource):
    @Park.expect(parser) #사용 인자는 parser에서 받은 인자들 
    @Park.response(200, 'Success') #성공시 응답 200
    @Park.response(500, 'Internal Server Error') #실패시 응답 500
    def get(self):
        args = parser.parse_args() #parse_args(): 'Python dictionary'타입으로 값을 리턴
        x = args['x']    
        y = args['y']
        dist = args['dist']
                             
        sql = 'SELECT * FROM park WHERE 1' #1은 항상 조건이 참이라는 의미 
        rows = app.app.database.execute(text(sql), {}).fetchall() #쿼리결과 fetchall로 모두 저장

        #쿼리 결과가 null
        if not rows:
            return {
                'code': 'error',
                'message': '(park table is empty) error'
            }, 500
             

        retVal = [] 
        for row in rows: 
            px = float(row['x']) #db에서 스트링으로 넘어온 값을 float으로 바꾼다
            py = float(row['y'])
            ud = distLL(x,y,px,py) #거리 계산

            if ud <= dist: #계산된 거리<=dist 참일때, 사용자 기준 dist(기본 1km)반경에 있는 공원
                r = {  #반환 값 생성 
                    'p_id':row['p_id'],
                    'p_name':row['p_name'],
                    'x': px,
                    'y': py,
                    'website': row['website']
                }
                retVal.append(r) 

        #retVal[] null
        if not retVal:
            return {
                'code': 'error',
                'message': '(parks do not exist) error'
            }, 500
                    
        return{                   
            'code':'successs',   #응답에 공원리스트인 retval을 넣어준다.
            'response': {
                'List': retVal
            }
        }, 200 

            
