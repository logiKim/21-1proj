#운동기구 이름-> 해당 운동기구가 있는 공원들의 위치 list를 얻어오는 파일
             
from flask_restx import Resource,reqparse
from .equip import Equip #namespace
import app                  
from sqlalchemy import text #db

parser = reqparse.RequestParser() 
parser.add_argument('e_name', help='운동기구 이름', type=str, required=True) #가져오는 정보:운동기구 이름

@Equip.route('/list')
class EquipList(Resource):
    @Equip.expect(parser)
    @Equip.response(200, 'Success')
    @Equip.response(500, 'Internal Server Error')
    def get(self):
        args = parser.parse_args()
        id = args['e_name']
           
        #equip 테이블을 기준으로 park테이블과 조인하여 해당 운동기구가 있는 공원 좌표를 얻어온다.
        sql = 'SELECT e_id,e_name,x,y,p_name FROM equip LEFT JOIN park ON equip.p_id = park.p_id WHERE e_name= :e_name'
        query = {
            'e_name': id
        }       
        rows = app.app.database.execute(text(sql), query).fetchall()
        
        #쿼리 결과가 null
        if not rows:
            return {
                'code': 'error',
                'message': '(no query results) error'
            }, 500
            

        retVal = []
        for row in rows:
          r = {        #row에 있는 정보를 r에 딕트로 저장하고 r을 생성해둔 retval=[]에 append한다 
                    'e_id'     : row['e_id'],
                    'e_name'   : row['e_name'],
                    'x'     : row['x'],
                    'y'     : row['y'],
                    'p_name'   : row['p_name']                                 
                }
          retVal.append(r)
        
        return { 
            'response': retVal     
        }, 200
  