ALTER TABLE Campus ADD COLUMN IF NOT EXISTS campus_id varchar(255) not null unique;
ALTER TABLE Collage ADD COLUMN IF NOT EXISTS collage_id varchar(255) not null unique;
ALTER TABLE End_User ADD COLUMN IF NOT EXISTS user_id varchar(255) not null unique;
ALTER TABLE Image ADD COLUMN IF NOT EXISTS image_id varchar(255) not null unique;
ALTER TABLE Item ADD COLUMN IF NOT EXISTS item_id varchar(255) not null unique;
ALTER TABLE Town ADD COLUMN IF NOT EXISTS town_id varchar(255) not null unique;
ALTER TABLE University ADD COLUMN IF NOT EXISTS university_id varchar(255) not null unique;