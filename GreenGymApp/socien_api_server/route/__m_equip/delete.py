from flask_restx import Resource,  reqparse
from ._m_equip import Mequip
import app
from sqlalchemy import text

#운동기구 id로 삭제 쿼리 실행
parser = reqparse.RequestParser()
parser.add_argument('e_id', help='운동기구 ID', type=int, required=True)

@Mequip.route('/delete')
class E_DELETE(Resource):
    @Mequip.expect(parser)
    @Mequip.response(200, 'Success')
    @Mequip.response(500, 'Internal Server Error')
    def get(self):
        args = parser.parse_args()
        e_id = args['e_id']

        #삭제할 e_id 행 조회
        sql = 'SELECT * FROM equip WHERE e_id=:e_id'
        query = {
            'e_id': e_id
        }
        row = app.app.database.execute(text(sql), query).fetchone()
        
        if row == None:
            return {
                'code': 'error',
                'message': '(e_id wrong)  no query result error'
            }, 500

        
 
        #삭제쿼리
        sql = 'DELETE FROM equip WHERE  e_id = :e_id'
        query = {
            'e_id': e_id
        }
        app.app.database.execute(text(sql), query)

        return {
            'code':'successs',
            'response': '',
         }, 200

        
       



        
        
        
