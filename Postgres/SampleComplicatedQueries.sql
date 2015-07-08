
SELECT 
        full_resolution_factory_content_group, 
        full_resolution_initiated_order, 
        data_layer, 
        feature_id, 
        (select md5(full_resolution_factory_content_group || full_resolution_initiated_order || data_layer))
FROM finished_nga_product_geometry 
WHERE is_multi_part = true  
AND full_resolution_initiated_order NOT LIKE 'po_%'  
AND feature_id != md5(full_resolution_factory_content_group || full_resolution_initiated_order || data_layer);

SELECT
        full_resolution_factory_content_group,
        full_resolution_initiated_order,
        data_layer,
        feature_id,
        (select md5(full_resolution_factory_content_group || full_resolution_initiated_order || data_layer))
FROM finished_product_geometry    
WHERE is_multi_part = true
AND full_resolution_initiated_order NOT LIKE 'po_%'
AND feature_id != md5(full_resolution_factory_content_group || full_resolution_initiated_order || data_layer);
