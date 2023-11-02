#공원id(p_id) -> 해당 공원에 있는 운동기구list를 가져오는 appi
from flask_restx import Resource, reqparse
from sqlalchemy.sql.type_api import NULLTYPE
from ._m_equip import Mequip
import app
from sqlalchemy import text


parser = reqparse.RequestParser()
parser.add_argument('id', help='공원 ID', type=int, required=True)

@Mequip.route('/p_e_list')
class P_E_List(Resource):
    @Mequip.expect(parser)
    @Mequip.response(200, 'Success')
    @Mequip.response(500, 'Internal Server Error')
    def get(self):
        args = parser.parse_args()
        id = args['id']

        sql = 'SELECT * FROM equip WHERE p_id=:id'
        query = {
            'id': id
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
            r = {        
                    'e_id'     : row['e_id'],
                    'p_id'     : row['p_id'],
                    'e_name'   : row['e_name'],
                    'category' : row['category']
                }
            retVal.append(r)
        

        return {                 
            'code':'successs',   
            'message':'',
            'response': {
                'List': retVal
            }
        }, 200

        
        
        
