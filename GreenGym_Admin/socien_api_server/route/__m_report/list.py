 #신고list를 얻어오는 API입니다
from flask_restx import Resource
from ._m_report import Mreport
import app             
from sqlalchemy import text

   

@Mreport.route('/list') 
class REPORT_List(Resource):
    @Mreport.response(200, 'Success')               #성공시 응답 200
    @Mreport.response(500, 'Internal Server Error') #실패시 응답 500
    def get(self):
                
        sql = 'SELECT * FROM report'
        rows = app.app.database.execute(text(sql),).fetchall()
        
        #쿼리결과 null
        if not rows:
             return {
                'code': 'error',
                'message': '(report table is empty) error'
            },500

        retVal = [] 
        for row in rows: 
            r = {                            
                    'r_id'        : row['r_id'],
                    'p_name'      : row['p_name'],
                    'r_text'      : row['r_text'],
                    'r_phone'     : row['r_phone'],
                    'r_name'      : row['r_name'],
                    'r_date'      : row['r_date']
                }
            retVal.append(r)

        

        return {                 
            'code'    :'successs',   
            'message' :'',
            'response': {
                'List': retVal
            }
        }, 200