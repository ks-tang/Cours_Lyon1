class PetaSkySchema:
    def __init__(self, object_schema_file, source_schema_file):
        self.object_schema_file = object_schema_file
        self.source_schema_file = source_schema_file
        self.object_attributes = []
        self.source_attributes = []
        self.index_by_attribute = {}  # Mapping from attribute name to index

    def read_schema(self):
        # Read Object schema
        with open(self.object_schema_file, 'r') as file:
            object_lines = file.readlines()[1:-1]

        # Read Source schema
        with open(self.source_schema_file, 'r') as file:
            source_lines = file.readlines()[1:-1]

        # Process Object schema
        for index, line in enumerate(object_lines):
            attribute_name = line.split()[0]
            self.index_by_attribute[attribute_name] = index
            self.object_attributes.append(attribute_name)

        # Process Source schema
        for index, line in enumerate(source_lines):
            attribute_name = line.split()[0]
            self.index_by_attribute[attribute_name] = index
            self.source_attributes.append(attribute_name)

# Example usage:
object_schema_file = "Object.sql"
source_schema_file = "Source.sql"
schema = PetaSkySchema(object_schema_file, source_schema_file)
schema.read_schema()

# Access the attributes and indices
object_attrs = schema.object_attributes
source_attrs = schema.source_attributes
index_of_attribute = schema.index_by_attribute

# Print the lists of attributes and their indices
print("Object Attributes:", object_attrs)
print("Source Attributes:", source_attrs)
print("Index by Attribute:", index_of_attribute)
