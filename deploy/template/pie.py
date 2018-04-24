import json

data = json.loads(model)
res = json.loads(template)
result = template

dat=[]
for i in range(1,len(data)):
    dat.append( {'name':data[i][0],'y':float(data[i][1])})

#result = json.dumps(dat)
res['series'] = [{'data':dat,"name": data[0][0],"colorByPoint": True}]
result = json.dumps(res)

