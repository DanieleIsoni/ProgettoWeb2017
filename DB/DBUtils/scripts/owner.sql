UPDATE pictures p
JOIN pictures_products pp
JOIN products pr
JOIN shops s
ON p.id=pp.id_picture
AND pp.id_product=pr.id
AND pr.id_shop=s.id
SET p.id_owner=s.id_owner,
p.name=pr.name, 
p.description=pr.name;
UPDATE pictures p
JOIN pictures_shops ps
JOIN shops s
ON p.id=ps.id_picture
AND ps.id_shop=s.id
SET
p.name= CONCAT("FotoNegozio: ",s.name),
p.description=CONCAT("FotoNegozio: ",s.name),
p.id_owner=s.id_owner
