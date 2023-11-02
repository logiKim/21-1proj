 #공원list
from flask_restx import Resource
from ._m_equip import Mequip
import app             
from sqlalchemy import text


@Mequip.route('/parklist') 
class ParkList(Resource):
    @Mequip.response(200, 'Success')              
    @Mequip.response(500, 'Internal Server Error')
    def get(self):

        sql = 'SELECT * FROM park'
        rows = app.app.database.execute(text(sql),).fetchall()

        #쿼리결과 null
        if not rows:
            return {
                'code': 'error',
                'message': '(park table is empty) error'
            }, 500

    
        retVal = [] 
        for row in rows: 
            r = {       
                    'p_id'     : row['p_id'],
                    'p_name'   : row['p_name'],
                    'x'        : row['x'],
                    'y'        : row['y'],
                    'website'  : row['website']                   
                }
            retVal.append(r)

        return {                       
            'code':'successs',   
            'message':'',
            'response': {
                'List': retVal
            }
        }, 200  