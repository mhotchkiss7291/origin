mysql -ugac_qa8_dply -pgaia-content_qa8 -hqa8mysqldb001.rstone.io gaia_content_qa8 -e "
SELECT Count(*)  counter,
       'courses' AS tabler
FROM   courses
WHERE  updated_at > Now() - INTERVAL 1 day
UNION
SELECT Count(*)        counter,
       'course_levels' AS tabler
FROM   course_levels
WHERE  updated_at > Now() - INTERVAL 1 day
UNION
SELECT Count(*)           counter,
       'course_sequences' AS tabler
FROM   course_sequences
WHERE  updated_at > Now() - INTERVAL 1 day
UNION
SELECT Count(*)    counter,
       'sequences' AS tabler
FROM   sequences
WHERE  updated_at > Now() - INTERVAL 1 day
UNION
SELECT Count(*)             counter,
       'sequence_activites' AS tabler
FROM   sequence_activities
WHERE  updated_at > Now() - INTERVAL 1 day
UNION
SELECT Count(*)     counter,
       'activities' AS tabler
FROM   activities
WHERE  updated_at > Now() - INTERVAL 1 day
UNION
SELECT Count(*)         counter,
       'activity_steps' AS tabler
FROM   activity_steps
WHERE  updated_at > Now() - INTERVAL 1 day" 
