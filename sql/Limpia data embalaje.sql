set foreign_key_checks=0;
delete from detail_packaging_caffee where remittances_caffee_id > 0;
delete from packaging_caffee where id > 0;
delete from adictional_elements_has_packaging_caffee where adictional_elements_id > 0;
update remittances_caffee set quantity_radicated_bag_out=0 where id >0;
set foreign_key_checks=1;